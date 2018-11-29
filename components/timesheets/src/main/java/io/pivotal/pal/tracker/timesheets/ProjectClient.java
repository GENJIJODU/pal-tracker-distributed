package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private final Map<Long,ProjectInfo> projectCache = new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo retrievedProjectInfo = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        projectCache.put(projectId, retrievedProjectInfo);
        return retrievedProjectInfo;
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        System.out.print("Retrieved cached project with id=" + projectId);
        return projectCache.get(projectId);
    }
}
