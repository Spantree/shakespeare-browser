class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "/shakespeare/plays"(controller: 'shakespeare', action: 'listPlays')
        "/shakespeare/lines"(controller: 'shakespeare', action: 'listLines')
	}
}
