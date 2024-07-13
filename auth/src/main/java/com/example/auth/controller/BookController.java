package com.example.auth.controller;

import com.example.auth.security.LogicEnum;
import com.example.auth.security.Permission;
import com.example.auth.security.PermissionsEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/book")
@RestController
public class BookController {

    @Permission(permissions = {PermissionsEnum.AllowRead, PermissionsEnum.AllowWrite,PermissionsEnum.AllowUpdate}, type = LogicEnum.All)
    @RequestMapping(method = RequestMethod.GET, path = "/all", produces = "application/json")
    public String getBook() {
        return "hello";
    }

    @Permission(permissions = {PermissionsEnum.AllowRead, PermissionsEnum.AllowWrite,PermissionsEnum.AllowUpdate}, type = LogicEnum.Any)
    @RequestMapping(method = RequestMethod.GET, path = "/any", produces = "application/json")
    public String getBook1() {
        return "hello";
    }

}
