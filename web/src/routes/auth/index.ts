import { createRoute } from "@tanstack/react-router";
import { authLayoutRoute } from "./layout";
import LoginPage from "@/pages/auth/LoginPage";

export const loginRoute = createRoute({
    getParentRoute: () => authLayoutRoute,
    path: "login",
    component: LoginPage,
});