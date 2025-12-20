import React, { useState } from "react";
import { Button } from "antd";
interface CustomButtonProps {
  color: string;   // màu chính
  hoverColor?: string; // màu hover
  icon?: React.ReactNode;
  label: string;
  onClick?: () => void;
}

const CustomButton: React.FC<CustomButtonProps> = ({
  color,
  hoverColor,
  icon,
  label,
  onClick,
}) => {
  const [hover, setHover] = useState(false);
  return (
    <Button
      icon={icon}
      type="primary"
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
