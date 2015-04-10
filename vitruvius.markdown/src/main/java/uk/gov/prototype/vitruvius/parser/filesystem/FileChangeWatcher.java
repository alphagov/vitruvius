package uk.gov.prototype.vitruvius.parser.filesystem;

import org.vertx.java.core.json.JsonObject;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.nio.file.*;
import java.util.Date;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileChangeWatcher implements Runnable {

    private String key;
    private Path path;
    private FSRepositoryInformationExtractor fsRepositoryInformationExtractor;

    public FileChangeWatcher(String key,
                             FSRepositoryInformationExtractor fsRepositoryInformationExtractor) {
        this.key = key;
        this.fsRepositoryInformationExtractor = fsRepositoryInformationExtractor;
        this.path = FileSystems.getDefault().getPath(key);
    }

    @Override
    public void run() {
        FileSystem fs = path.getFileSystem();
        try (WatchService service = fs.newWatchService()) {
            path.register(service, ENTRY_CREATE, ENTRY_MODIFY);
            WatchKey watchKey;
            while (true) {
                watchKey = service.take();
                WatchEvent.Kind<?> kind = null;
                boolean requiredReload = false;
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    kind = watchEvent.kind();
                    if (OVERFLOW == kind) {
                        continue; //loop
                    } else if (ENTRY_MODIFY == kind && (watchEvent.context().toString().contains("vitruvius.md"))) {
                        requiredReload = true;
                        break;
                    }
                }
                boolean isReset = watchKey.reset();
                if (requiredReload) {
                    JsonObject jsonObject = createEventPayload();
                    RepositoryInformation repositoryInformation = fsRepositoryInformationExtractor.getInformationCache().get(key);
                    RepositoryInformation loaded = fsRepositoryInformationExtractor.get(new RepositoryUri(repositoryInformation.getServiceName(), key));
                    fsRepositoryInformationExtractor.getInformationCache().populateCache(loaded);
                    fsRepositoryInformationExtractor.getRepositoryESOperations().register(loaded);
                    fsRepositoryInformationExtractor.getVertx().eventBus().publish("vitruvius.data.reloaded", jsonObject.encode());
                }
                if (!isReset) {
                    break; //loop
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JsonObject createEventPayload() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.putString("uri", key);
        jsonObject.putString("updatedBy", System.getProperty("user.name"));
        jsonObject.putString("updatedOn", new Date().toString());
        return jsonObject;
    }
}
