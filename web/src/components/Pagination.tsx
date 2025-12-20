import React from "react";
import { ArrowLeftOutlined, ArrowRightOutlined } from "@ant-design/icons";
import { Select, Button } from "antd";
import { colors } from "@/theme/color";

interface PaginationComponentProps {
  currentPage: number;
  totalItems: number;
  itemsPerPage: number;
  onPageChange: (page: number) => void;
  onItemsPerPageChange: (items: number) => void;
}

const PaginationComponent: React.FC<PaginationComponentProps> = ({
  currentPage,
  totalItems,
  itemsPerPage,
  onPageChange,
  onItemsPerPageChange,
}) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  const handlePrevious = () => {
    if (currentPage > 1) onPageChange(currentPage - 1);
  };

  const handleNext = () => {
    if (currentPage < totalPages) onPageChange(currentPage + 1);
  };

  const handlePageClick = (page: number) => {
    onPageChange(page);
  };

  const handleItemsPerPageChange = (value: number) => {
    onItemsPerPageChange(value);
    onPageChange(1);
  };

  const renderPageNumbers = () => {
    const pageNumbers: (number | string)[] = [];
    const maxPagesToShow = 5;

    if (totalPages <= maxPagesToShow) {
      for (let i = 1; i <= totalPages; i++) pageNumbers.push(i);
    } else {
      const left = Math.max(2, currentPage - 1);
      const right = Math.min(totalPages - 1, currentPage + 1);
      pageNumbers.push(1);
      if (left > 2) pageNumbers.push("...");
      for (let i = left; i <= right; i++) pageNumbers.push(i);
      if (right < totalPages - 1) pageNumbers.push("...");
      pageNumbers.push(totalPages);
    }

    return pageNumbers.map((p, index) =>
      p === "..." ? (
        <span key={`dots-${index}`} className="px-3 text-gray-400">
          ...
        </span>
      ) : (
        <Button
          key={`page-${p}-${index}`}
          size="small"
          type={currentPage === p ? "primary" : "default"}
          onClick={() => handlePageClick(p as number)}
          style={{
            backgroundColor:
              currentPage === p ? colors.blue.b2 : "white",
            borderColor:
              currentPage === p ? colors.blue.b2 : "#d1d5db",
            color: currentPage === p ? "#fff" : "#4b5563",
            transition: "all 0.25s ease",
            height: 32,
            width: 32,
          }}
          onMouseEnter={(e) => {
            const btn = e.currentTarget;
            btn.style.borderColor = colors.blue.b2;
            btn.style.color =
              currentPage === p ? "#fff" : colors.blue.b2;
          }}
          onMouseLeave={(e) => {
            const btn = e.currentTarget;
            btn.style.borderColor =
              currentPage === p ? colors.blue.b2 : "#d1d5db";
            btn.style.color =
              currentPage === p ? "#fff" : "#4b5563";
          }}
        >
          {p}
        </Button>
      )
    );
  };

  return (
    <div className="flex justify-end items-center gap-4 mt-4">
      {/* Select số dòng / trang */}
      <Select
        value={itemsPerPage}
        onChange={handleItemsPerPageChange}
        style={{

          borderColor: colors.blue.b2,
        }}
        options={[
          { value: 1, label: "1" },
          { value: 10, label: "10" },
          { value: 20, label: "20" },
          { value: 50, label: "50" },
        ]}
      />

      {/* Nút phân trang */}
      <div className="flex items-center gap-1">
        <Button
          size="small"
          icon={<ArrowLeftOutlined />}
          disabled={currentPage === 1}
          onClick={handlePrevious}
          style={{
            borderColor: colors.blue.b2,
            color:
              currentPage === 1 ? colors.gray.g1 : colors.blue.b2,
            backgroundColor: "white",
            height: 32,
          }}
          onMouseEnter={(e: React.MouseEvent<HTMLButtonElement>) => {
            if (!e.currentTarget.disabled)
              e.currentTarget.style.backgroundColor = `${colors.blue.b2}15`;
          }}
          onMouseLeave={(e: React.MouseEvent<HTMLButtonElement>) => {
            e.currentTarget.style.backgroundColor = "white";
          }}
        >
          Trước
        </Button>

        {renderPageNumbers()}

        <Button
          size="small"
          icon={<ArrowRightOutlined />}
          disabled={currentPage === totalPages}
          onClick={handleNext}
          style={{
            borderColor: colors.blue.b2,
            height: 32,
            color:
              currentPage === totalPages
                ? colors.gray.g1
                : colors.blue.b2,
            backgroundColor: "white",
          }}
          onMouseEnter={(e: React.MouseEvent<HTMLButtonElement>) => {
            if (!e.currentTarget.disabled)
              e.currentTarget.style.backgroundColor = `${colors.blue.b2}15`;
          }}
          onMouseLeave={(e: React.MouseEvent<HTMLButtonElement>) => {
            e.currentTarget.style.backgroundColor = "white";
          }}
        >
          Sau
        </Button>
      </div>
    </div>
  );
};

export default PaginationComponent;
