"use client";

import { getLeaveRequestsSummary } from "@/services/leaveRequest.service";
import {
  LeaveRequestsSummary
} from "@/types/leave/leaveRequest";
import { PageResponse } from "@/types/pagination/page";
import { useState } from "react";
import LeaveRequestsList from "./LeaveRequestsList";
import Button from "./ui/Button";

type Props = {
  initialPage: PageResponse<LeaveRequestsSummary>;
  role: Role;
  detailBasePath: string;
  hrPolicy: string;
};

export default function LeaveRequestsListWithLoadMore({
  initialPage,
  role,
  detailBasePath,
  hrPolicy,
}: Props) {
  const [requests, setRequests] = useState(initialPage.content);
  const [page, setPage] = useState(initialPage.number);
  const [isLast, setIsLast] = useState(initialPage.last);
  const [loading, setLoading] = useState(false);

  const handleLoadMore = async () => {
    setLoading(true);

    try {
      const nextPage = page + 1;

      const response = await getLeaveRequestsSummary(nextPage, 10);

      setRequests((prev) => [...prev, ...response.content]);
      setPage(response.number);
      setIsLast(response.last);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <LeaveRequestsList
        leaveRequests={requests}
        role={role}
        detailBasePath={detailBasePath}
        hrPolicy={hrPolicy}
      />

      {!isLast && (
        <div className="flex justify-center mt-6">
          <Button
            onClick={handleLoadMore}
            disabled={loading}
            className="text-sm self-center bg-[var(--color-dark-purple)] text-[var(--color-block-white)]"
            width="w-24"
            paddingY="p-1"
            title={loading ? "Chargement..." : "Voir plus"}
          />
        </div>
      )}
    </>
  );
}
