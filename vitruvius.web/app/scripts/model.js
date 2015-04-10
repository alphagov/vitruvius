ServiceRepository = function (serviceName, repoName, repoUrl, link, metaData, commitInfo) {
    this.serviceName = serviceName;
    this.repoName = repoName;
    this.repoUrl = repoUrl;
    this.link = link;
    this.metaData = metaData;
    this.commitInfo = commitInfo;
}


ServiceRepository.build = function (rawData) {
    var repositoryData = [];
    angular.forEach(JSON.parse(rawData), function (data) {
        var collaboratingServices = [];
        angular.forEach(data.services, function (service) {
            collaboratingServices.push(new CollaboratingService(service.name, service.type, service.link));
        });
        var collaboratingComponents = [];
        angular.forEach(data.components, function (component) {
            collaboratingComponents.push(new CollaboratingService(component.name, component.type, component.link));
        });
        var datas = [];
        angular.forEach(data.data, function (dataRepo) {
            datas.push(new CollaboratingService(dataRepo.name, dataRepo.type, dataRepo.link));
        });

        var serviceMeta =
            new ServiceMetaData(data.meta.description, data.meta.author, data.meta.status, data.meta.lastUpdated, collaboratingServices,collaboratingComponents,datas, data.meta.department, data.meta.type);
        var repoInfo = new ServiceRepository(data.serviceName, data.repoName, data.repoUri, data.link, serviceMeta, []);
        repositoryData.push(repoInfo);
    });

    return repositoryData;
}


ServiceMetaData = function (description, author, status, lastUpdatedOn, collaboratingServices,collaboratingComponents,  datas, department, type) {
    this.description = description;
    this.author = author;
    this.status = status;
    this.lastUpdatedOn = lastUpdatedOn;
    this.collaboratingServices = collaboratingServices;
    this.collaboratingComponents = collaboratingComponents;
    this.datas = datas;
    this.type = type;
    this.department = department;

}

CollaboratingService = function (name, type, link) {
    this.name = name;
    this.type = type;
    this.link = link;

}




getLabel = function(field) {
    if (field === 'author') {
        return "Author";
    } else if (field === 'department') {
        return "Department";
    }  else if (field === 'lastUpdatedOn') {
        return "LastUpdated";
    } else if (field === 'status') {
        return "Status";
    } else {
        return "Unknown";
    }

}

