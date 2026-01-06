import React, { useState } from "react";
import { Button } from "antd";
interface CustomButtonProps {
  color?: string;   // màu chính
  hoverColor?: string; // màu hover
  icon?: React.ReactNode;
  loading?: boolean;
  label: string;
  onClick?: () => void;
  size?: "large" | "middle" | "small";
  className?: string;
  htmlType?: "button" | "submit" | "reset";
}

const CustomButton: React.FC<CustomButtonProps> = ({
  color,
  hoverColor,
  icon,
  loading = false,
  label,
  onClick,
  size = "middle",
  className,
  htmlType = "button", 
}) => {
  const [hover, setHover] = useState(false);
  return (
    <Button
      icon={icon}
      type="primary"
      size={size}
      className={className}
      loading={loading}
      htmlType={htmlType}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
      style={{
        backgroundColor: hover ? hoverColor : color,
        border: `1px solid ${hover ? hoverColor : color}`,
        color: "white",
        transition: "all 0.25s ease",
      }}
      onClick={onClick}
    >
      {label}
    </Button>
  );
};
export default CustomButton;
