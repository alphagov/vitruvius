package uk.gov.prototype.vitruvius.parser.validator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.vertx.java.core.json.DecodeException;
import uk.gov.prototype.vitruvius.parser.VitruviusParser;
import uk.gov.prototype.vitruvius.parser.domain.CollaboratingProject;
import uk.gov.prototype.vitruvius.parser.domain.Meta;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformation;
import uk.gov.prototype.vitruvius.parser.domain.RepositoryInformationBuilder;
import uk.gov.prototype.vitruvius.parser.github.GitHubUriParser;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContentValidator {

    public List<ValidationMessage> validate(RepositoryInformation repositoryInformation) {
        List<ValidationMessage> messages = new ArrayList<>();
        validateMetaData(messages, repositoryInformation);
        validateServicesAndComponents(messages, repositoryInformation);
        return messages;
    }

    public List<ValidationMessage> validate(String content) {
        List<ValidationMessage> messages = new ArrayList<>();
        RepositoryInformationBuilder repositoryInformationBuilder = new RepositoryInformationBuilder();
        try {
            VitruviusParser.getMarkdownProcessor(repositoryInformationBuilder).process(content);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DecodeException) {
                DecodeException decodeException = (DecodeException) e;
                messages.add(new ValidationMessage(decodeException.getMessage(), ValidationMessage.ValidationType.ERROR));
            }
        }

        RepositoryInformation repositoryInformation = repositoryInformationBuilder.build();

        messages.addAll(validate(repositoryInformation));
        return messages;
    }

    private void validateServicesAndComponents(List<ValidationMessage> messages, RepositoryInformation repositoryInformation) {

        List<CollaboratingProject> collaboratingProjects = new ArrayList<>();
        collaboratingProjects.addAll(repositoryInformation.getServices());
        collaboratingProjects.addAll(repositoryInformation.getComponents());
        if (collaboratingProjects.size() != 0) {
            for (CollaboratingProject collaboratingProject : collaboratingProjects) {
                if (!serviceLinkIsValid(collaboratingProject)) {
                    messages.add(new ValidationMessage("Collaborating Project link '" + collaboratingProject.getLink() + "' is not valid URI", ValidationMessage.ValidationType.WARNING));
                }

            }
        }

    }

    private boolean serviceLinkIsValid(CollaboratingProject collaboratingProject) {
        if (collaboratingProject.getLink().startsWith("https://") || collaboratingProject.getLink().startsWith("http://")) {
            GitHubUriParser gitHubUriParser = new GitHubUriParser(collaboratingProject.getLink());
            GitHubClient gitHubClient = new GitHubClient(gitHubUriParser.clientHost());
            RepositoryService repositoryService = new RepositoryService(gitHubClient);
            try {
                Repository repository = repositoryService.getRepository(gitHubUriParser.owner(), gitHubUriParser.repoName());
                if (repository != null) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Path path = FileSystems.getDefault().getPath(collaboratingProject.getLink());
            if (path.toFile().exists()) {
                return true;
            }
        }
        return false;
    }

    private void validateMetaData(List<ValidationMessage> messages, RepositoryInformation repositoryInformation) {
        if (repositoryInformation.getMeta() == null) {
            messages.add(new ValidationMessage("Missing meta data section", ValidationMessage.ValidationType.ERROR));
        } else {
            validateMetaDataContent(repositoryInformation.getMeta(), messages);
        }
    }

    private void validateMetaDataContent(Meta meta, List<ValidationMessage> messages) {
        if (StringUtils.isEmpty(meta.getAuthor())) {
            messages.add(new ValidationMessage("Missing author in meta data", ValidationMessage.ValidationType.ERROR));
        }
        if (StringUtils.isEmpty(meta.getStatus())) {
            messages.add(new ValidationMessage("Missing status in meta data", ValidationMessage.ValidationType.ERROR));
        }
        if (StringUtils.isEmpty(meta.getDescription())) {
            messages.add(new ValidationMessage("Missing description in meta data", ValidationMessage.ValidationType.WARNING));
        }

        if (StringUtils.isEmpty(meta.getDepartment())) {
            messages.add(new ValidationMessage("Missing department in meta data", ValidationMessage.ValidationType.ERROR));
        }

        if (StringUtils.isEmpty(meta.getType())) {
            messages.add(new ValidationMessage("Missing type in meta data", ValidationMessage.ValidationType.ERROR));
        }else if(!Meta.MetaType.isValidTypeByString(meta.getType())){
            messages.add(new ValidationMessage("Type '"+meta.getType()+"' in meta data is not valid please use: "+ Meta.MetaType.SERVICE.getStringRepresentation()+" or " + Meta.MetaType.COMPONENT.getStringRepresentation(), ValidationMessage.ValidationType.ERROR));
        }

    }


}
