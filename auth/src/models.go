package main

type login struct {
	ID       string `json:"id"`
	Username string `json:"username"`
	Password string `json:"password"`
}

type register struct {
	ID              string `json:"id"`
	Name            string `json:"name"`
	Username        string `json:"username"`
	Password        string `json:"password"`
	PermissionLevel int    `json:"permission_level"`
}

type token struct {
	Token string `json:"token"`
}
