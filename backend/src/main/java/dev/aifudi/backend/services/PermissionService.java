package dev.aifudi.backend.services;

import dev.aifudi.backend.dtos.request.UserRegisterRequestDTO;
import dev.aifudi.backend.entities.Role;
import dev.aifudi.backend.entities.User;
import dev.aifudi.backend.repositories.RoleRepositoryImp;
import dev.aifudi.backend.services.exceptions.AccessDeniedException;
import dev.aifudi.backend.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    private final RoleRepositoryImp roleRepositoryImp;

    public PermissionService(RoleRepositoryImp roleRepositoryImp) {
        this.roleRepositoryImp = roleRepositoryImp;
    }

    public void checkRegisterUserPermission(UserRegisterRequestDTO userRequest){
        if(userRequest.roleName().equals("admin")){
            throw new AccessDeniedException("Not allowed to create a admin user");
        }
    }

     public void checkRegisterUpdatePermission(User authUser, String requestEmail){
        Optional<Role> userRole = this.roleRepositoryImp.findRoleById(authUser.getRoleId());
        if(userRole.isEmpty()){
            throw new NotFoundException("Role not found");
        }

         boolean isAdmin = userRole.get().getName().equals("admin");
         boolean isOwnerOfAccount = authUser.getEmail().equals(requestEmail);

         if (!isAdmin && !isOwnerOfAccount) {
             throw new AccessDeniedException("Not allowed to change another user's data");
         }
     }

     public void checkDeleteUserPermission(User authUser, String requestEmail){
         Optional<Role> userRole = this.roleRepositoryImp.findRoleById(authUser.getRoleId());
         if(userRole.isEmpty()){
             throw new NotFoundException("Role not found");
         }

         boolean isAdmin = userRole.get().getName().equals("admin");
         boolean isOwnerOfAccount = authUser.getEmail().equals(requestEmail);

         if (!isAdmin && !isOwnerOfAccount) {
             throw new AccessDeniedException("Not allowed to delete another user's register");
         }
     }
}
