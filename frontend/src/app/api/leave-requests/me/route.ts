"use server"

import { CreateLeaveRequestReviewPayload, LeaveRequest, LeaveRequestsSummary, MyLeaveRequestsSummary } from "@/types/leave/leaveRequest";
import { cookies } from "next/headers";

  
  export async function getMyLeaveRequestsSummary(): Promise<MyLeaveRequestsSummary[]> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests/me/summary`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        cache: "no-store",
      }
    );
  
    if (!res.ok) {
      throw new Error("Error occuring while loading leave request data");
    }

    return res.json();
  }

  export async function getLeaveRequestsSummary(): Promise<LeaveRequestsSummary[]> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests/summary`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        cache: "no-store",
      }
    );
  
    if (!res.ok) {
      throw new Error("Error occuring while loading leave request data");
    }

    return res.json();
  }

  export async function getLeaveRequestDetail(leaveRequestId: string): Promise<LeaveRequest> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests/${leaveRequestId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        cache: "no-store",
      }
    );
  
    if (!res.ok) {
      throw new Error("Error occuring while loading leave request data");
    }

    return res.json();
  }

  export async function createLeaveRequestReview(leaveRequestId: number,
    payload: CreateLeaveRequestReviewPayload): Promise<LeaveRequest> {
    const cookieStore = await cookies();
    const token = cookieStore.get("access_token")?.value;
  
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/leave-requests/${leaveRequestId}/review`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      }
    );
  
    if (!res.ok) {
      throw new Error("Error occuring while creating leave request review");
    }

    return res.json();
  }
  