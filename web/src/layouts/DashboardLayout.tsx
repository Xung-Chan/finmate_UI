import { useEffect, useState } from "react";
import { DashboardOutlined, TeamOutlined, WalletOutlined, TransactionOutlined, BellOutlined } from "@ant-design/icons";
import { Outlet, useNavigate, useLocation } from "@tanstack/react-router";
import { useLogout } from "@/hooks/auth.hook";
import logo from "@/assets/Logomark.png";
import { colors } from "@/theme/color";

function getMenuStateFromPath(pathname: string) {
    const parts = pathname.split("/").filter(Boolean);
    const parentKey = parts[1] || "tong-quan";
    const childKey = parts[2] || null;
    return {
        selectedKey: childKey ? `${parentKey}/${childKey}` : parentKey,
        openMenuKey: childKey ? parentKey : null,
    };
}

const DashboardLayout = () => {
    const [selectedKey, setSelectedKey] = useState("tong-quan");
    const [openMenuKey, setOpenMenuKey] = useState<string | null>(null);
    const navigate = useNavigate();
    const location = useLocation();
    const logout = useLogout();
    useEffect(() => {
        const { selectedKey, openMenuKey } = getMenuStateFromPath(location.pathname);
        setSelectedKey(selectedKey);
        setOpenMenuKey(openMenuKey);
    }, [location.pathname]);

    const mainMenuItems = [
        {
            key: "analytics",
            label: "Tổng quan",
            icon: <DashboardOutlined />,
        },
        {
            key: "users",
            label: "Quản lý người dùng",
            icon: <TeamOutlined />,
            subMenu: [
                { key: "list", label: "Danh sách người dùng" },
                { key: "logout", label: "Đăng xuất" },
            ],
        },
        {
            key: "wallets-management",
            label: "Quản lý ví",
            icon: <WalletOutlined />,
            subMenu: [
                { key: "wallets", label: "Ví thường" },
                { key: "wallet-verifications", label: "Xác minh ví" },
                { key: "paylaters", label: "Ví trả sau" },
                { key: "paylater-applications", label: "Đơn ví trả sau" },
            ],
        },
        {
            key: "transactions",
            label: "Quản lý giao dịch",
            icon: <TransactionOutlined />,
        },
        {
            key: "notifications",
            label: "Quản lý thông báo",
            icon: <BellOutlined />,
            subMenu: [
                { key: "list", label: "Danh sách thông báo" },
                { key: "log-mails", label: "Nhật ký email" },
            ],
        },

        // {
        //     key: "ai-training-data",
        //     label: "Nội dung AI",
        //     icon: <UsergroupAddOutlined />,
        // },
        // {
        //     key: "credit-policy",
        //     label: "Chính sách tín dụng",
        //     icon: <UsergroupAddOutlined />,
        // },
    ];

    const handleMenuClick = (item: any) => {
        setSelectedKey(item.key);
        if (item.subMenu) {
            setOpenMenuKey(openMenuKey === item.key ? null : item.key);
        } else {
            setOpenMenuKey(null);
            navigate({ to: `/manage/${item.key}` });
        }
    };

    const handleSubMenuClick = (parentKey: string, key: string) => {
        if (key === "logout") {
            logout();
            return;
        }

        setSelectedKey(`${parentKey}/${key}`);
        navigate({ to: `/manage/${parentKey}/${key}` });
    };



    return (
        <div className="flex h-screen bg-gray-100 overflow-hidden">
            {/* SIDEBAR */}
            <aside
                className="relative w-56 bg-white flex flex-col h-full rounded-[10px] p-2 shadow-xl z-10"
                style={{
                    border: `1px solid ${colors.blue.b1}`
                }}
            >

                {/* Logo */}
                <div className="flex items-center justify-center h-24 border-b border-gray-200">
                    <img src={logo} alt="Logo" className="h-12" />
                </div>

                {/* Menu */}
                <nav className="flex-1 px-3 py-4 overflow-y-auto">
                    {mainMenuItems.map((item) => (
                        <div key={item.key}>
                            <div
                                className={`flex items-center pl-4 py-3 mb-2 rounded-md cursor-pointer transition-all duration-200`}
                                style={{
                                    background:
                                        selectedKey === item.key || selectedKey.startsWith(`${item.key}/`)
                                            ? `linear-gradient(to right, ${colors.blue.b1}22, ${colors.blue.b6}11)` // đuôi "22" và "11" là opacity (hex)
                                            : "transparent",
                                    color:
                                        selectedKey === item.key || selectedKey.startsWith(`${item.key}/`)
                                            ? colors.blue.b1
                                            : colors.gray.g1,
                                }}
                                onMouseEnter={(e) => {
                                    (e.currentTarget.style.background = `linear-gradient(to right, ${colors.blue.b1}22, ${colors.blue.b6}11)`);
                                    (e.currentTarget.style.color = colors.blue.b1);
                                }}
                                onMouseLeave={(e) => {
                                    if (
                                        !(selectedKey === item.key || selectedKey.startsWith(`${item.key}/`))
                                    ) {
                                        e.currentTarget.style.background = "transparent";
                                        e.currentTarget.style.color = colors.gray.g1;
                                    }
                                }}
                                onClick={() => handleMenuClick(item)}
                            >
                                <span className="text-lg">{item.icon}</span>
                                <span className="ml-2 text-sm">{item.label}</span>
                            </div>


                            {item.subMenu && openMenuKey === item.key && (
                                <div className="ml-4 pl-2" style={{ borderLeft: `2px solid ${colors.blue.b1}` }}>
                                    {item.subMenu.map((subItem) => (
                                        <div
                                            key={subItem.key}
                                            className="p-2 mb-1 rounded-md cursor-pointer text-sm transition-all"
                                            style={{
                                                background:
                                                    selectedKey === `${item.key}/${subItem.key}`
                                                        ? `${colors.blue.b1}22`
                                                        : "transparent",
                                                color:
                                                    selectedKey === `${item.key}/${subItem.key}`
                                                        ? colors.blue.b1
                                                        : colors.gray.g1,
                                            }}
                                            onMouseEnter={(e) => (e.currentTarget.style.color = colors.blue.b1)}
                                            onMouseLeave={(e) => {
                                                if (selectedKey !== `${item.key}/${subItem.key}`)
                                                    e.currentTarget.style.color = colors.gray.g1;
                                            }}
                                            onClick={() => handleSubMenuClick(item.key, subItem.key)}
                                        >
                                            {subItem.label}
                                        </div>

                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </nav>
            </aside>

            {/* MAIN CONTENT */}
            <div
                className="flex-1 flex flex-col bg-gray-100 rounded-[10px] z-0"


            >
                {/* HEADER */}
                <header
                    className="fixed top-0 left-0 w-full h-20 flex items-center justify-end px-12 z-10"
                >
                    <div className="flex items-center space-x-3 text-gray-700 font-bold">
                        <span className="font-medium">Nguyen Van A</span>
                        <img
                            src={logo}
                            alt="Avatar"
                            className="w-9 h-9 rounded-full border border-white/30"
                        />
                    </div>
                </header>
                <main className="flex-1 p-4 overflow-y-auto z-20">
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default DashboardLayout;