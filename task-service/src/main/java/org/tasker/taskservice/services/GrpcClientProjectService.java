package org.tasker.taskservice.services;

import java.util.List;

public interface GrpcClientProjectService {

    List<String> getProjectMembers(Long projectId);

    boolean existsByProjectId(Long projectId);
}
