package uk.gov.prototype.vitruvius.parser.github;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryHook;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.shareddata.ConcurrentSharedMap;
import org.vertx.java.core.shareddata.SharedData;
import uk.gov.prototype.vitruvius.parser.DateParser;
import uk.gov.prototype.vitruvius.parser.RepositoryInformationExtractor;
import uk.gov.prototype.vitruvius.parser.VitruviusParser;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryUri;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GitHubRepositoryInformationExtractor implements RepositoryInformationExtractor {

    private RepositoryContainerService repositoryContainerService;
    private static final Logger logger = LoggerFactory.getLogger(GitHubRepositoryInformationExtractor.class);

    public GitHubRepositoryInformationExtractor(RepositoryContainerService repositoryContainerService) {
        this.repositoryContainerService = repositoryContainerService;
    }

    public static class RepositoryContainerService {

        private ConcurrentSharedMap<String, RepositoryContainer> sharedMap;
        private ConcurrentSharedMap<String, String> config;

        public RepositoryContainerService(SharedData sharedData) {
            sharedMap = sharedData.getMap("repositories");
            config = sharedData.getMap("config");
        }

        public RepositoryContainer getRepositoryContainer(RepositoryUri repositoryUri) throws Exception {
            GitHubUriParser gitHubUriParser = new GitHubUriParser(repositoryUri.getUri());
            if (!sharedMap.containsKey(repositoryUri.getUri())) {
                GitHubClient gitHubClient = new GitHubClient(gitHubUriParser.clientHost());
                gitHubClient.setOAuth2Token(config.get("github.token"));
                RepositoryService repositoryService = new RepositoryService(gitHubClient);
                Repository repository = repositoryService.getRepository(gitHubUriParser.owner(), gitHubUriParser.repoName());
                sharedMap.put(repositoryUri.getUri(), new RepositoryContainer(repository, gitHubClient));
            }

            return sharedMap.get(repositoryUri.getUri());

        }

        public void createWebHooks(RepositoryContainer repositoryContainer) throws Exception {
            RepositoryService repositoryService = new RepositoryService(repositoryContainer.gitHubClient);
            Repository repository = repositoryContainer.repository;
            List<RepositoryHook> hooks = repositoryService.getHooks(repository);
            boolean webHookExists = false;
            String hookName = "web"; //It seems as though if you use any other name Github complains...no idea why. Couldn't find anything on Google about it

            for (RepositoryHook hook : hooks) {
                if (hook.getName().equals(hookName)) {
                    webHookExists = true;
                }
            }
            String url = config.get("vitruvius.post.address");
            logger.info("URL for post '{}'", url);
            if (url == null) {
                throw new RuntimeException("No url defined");
            }

            if (!webHookExists) {
                logger.info("No hook for '{}' so creating one", repository.getName());
                RepositoryHook newHook = new RepositoryHook();
                newHook.setName(hookName);

                newHook.setUrl(url);
                newHook.setActive(true);
                newHook.setId(System.currentTimeMillis() + 1);
                Map<String, String> config = new HashMap<>();
                config.put("url", url);
                config.put("content_type", "json");
                newHook.setConfig(config);
                newHook.setCreatedAt(new Date());

                repositoryService.createHook(repository, newHook);
            } else {

                logger.info("Hook exists  for '{}'", repository.getName());
            }
        }


    }

    public RepositoryInformation get(RepositoryUri repositoryUri) throws Exception {
        GitHubUriParser gitHubUriParser = new GitHubUriParser(repositoryUri.getUri());
        RepositoryContainer repositoryContainer = repositoryContainerService.getRepositoryContainer(repositoryUri);
        Repository repository = repositoryContainer.repository;
        ContentsService contentService = new ContentsService(repositoryContainer.gitHubClient);
        List<RepositoryContents> contents = contentService.getContents(repository, "vitruvius.md");

        if (contents != null && contents.size() != 0) {
            try {
                RepositoryContents repositoryContents = contents.get(0);
                String content = repositoryContents.getContent();
                String markdown = new String(org.apache.commons.codec.binary.Base64.decodeBase64(content.getBytes()), "UTF-8");
                RepositoryInformationBuilder repositoryInformationBuilder = VitruviusParser.generateRepositoryInformationFrom(markdown);
                RepositoryInformation repositoryInformation = repositoryInformationBuilder.link(repository.getHtmlUrl())
                        .repoUri(gitHubUriParser.repoUri())
                        .setDescription(repository.getDescription())
                        .setLastUpdated(DateParser.parse(repository.getUpdatedAt()))
                        .serviceName(repositoryUri.getName())
                        .markdown(markdown)
                        .build();
                logger.info("created repository information '{}'", repositoryInformation);
                try {
                    repositoryContainerService.createWebHooks(repositoryContainer);
                } catch (Exception e) {
                    //TODO this exception will always be called currently as the functionality requires a username, password and oauth token
                    logger.error("Unable to create webhooks for repository but still registering the repository with Vitruvius");
                }
                return repositoryInformation;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GitHubInteractionException("Unable to parse content from url " + e.getMessage());
            }
        }
        throw new GitHubInteractionException("failed to find the vitruvius.md file in repository " + repositoryUri.getUri());

    }


}
