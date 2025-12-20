import { RouterProvider } from "@tanstack/react-router";
import { Toaster } from "react-hot-toast";
import "./App.css";
import { router } from "./router";
import { App as AntdApp, ConfigProvider } from "antd";
function App() {
    return (
        <ConfigProvider>
            <AntdApp>
                <RouterProvider router={router} />
                <Toaster />
            </AntdApp>
        </ConfigProvider>
    );
}

export default App;
