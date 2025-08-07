package com.project.hiptour.sync.application;

import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/sync")
public class AdminSyncController {
    private final SyncPlaceCommandHandler syncPlaceCommandHandler;

    @PostMapping("/places")
    public ResponseEntity<?> syncPlaces(/**@AuthenticationPrincipal**/User user) {
        //if (!user.isAdmin()) return ResponseEntity.status(403).build();

        syncPlaceCommandHandler.sync();
        return ResponseEntity.ok("동기화 완료");
    }
}
