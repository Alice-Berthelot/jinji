"use client";

import { getMyLeaveRequestDetail } from "@/app/api/leave-requests/me/route";
import LeaveRequestDetail from "@/components/LeaveRequestDetail";
import BackArrow from "@/components/ui/BackArrow";
import MainTitle from "@/components/ui/MainTitle";
import { MyLeaveRequestDetail } from "@/types/leave/leaveRequest";
import { useParams } from "next/navigation";
import { useState, useEffect } from "react";

export default function LeaveRequestDetailPage() {
  const params = useParams();
const leaveRequestId = params.id as string;
  const [leaveRequest, setLeaveRequest] = useState<
    MyLeaveRequestDetail | null
  >(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        const data = await getMyLeaveRequestDetail(leaveRequestId);
        setLeaveRequest(data);
        setLoading(false);
      } catch (err) {
        console.error(err);
      }
    }
    load();
  }, []);
  return (
    <>
      <BackArrow />
      <MainTitle title={`Demande d'absence n°${params.id}`} />
      <section className="m-auto lg:my-0 lg:mx-8 bg-[var(--color-block-white)] px-6 py-4 shadow-sm rounded-sm w-[95%] lg:min-h-screen">
        <LeaveRequestDetail leaveRequest={leaveRequest} loading={loading} />
      </section>
    </>
  );
}
