import { createRootRoute, redirect } from "@tanstack/react-router";

export const rootRoute = createRootRoute({
    beforeLoad: ({location}) => {
        if (location.pathname === "/") {
            return redirect({to :"/auth/login"});
        }
    },
})