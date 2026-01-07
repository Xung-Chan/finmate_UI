import React from 'react';

const NoAccess: React.FC<{ message?: string }> = ({ message }) => {
  return (
    <div className="w-full h-64 flex items-center justify-center">
      <div className="text-center">
        <h3 className="text-xl font-semibold">Không có quyền truy cập</h3>
        <p className="text-sm text-gray-500">{message ?? 'Bạn không có quyền xem nội dung này.'}</p>
      </div>
    </div>
  );
};

export default NoAccess;
