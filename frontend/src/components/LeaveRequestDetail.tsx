"use client"

import { createLeaveRequestReview } from "@/app/api/leave-requests/me/route";
import { LeaveRequest } from "@/types/leave/leaveRequest";
import { formatDate } from "@/utils/formatDate";
import { formatLeaveReview } from "@/utils/formatLeaveReview";
import { formatPeriod } from "@/utils/formatPeriod";
import { useEffect, useState } from "react";
import Button from "./ui/Button";
import { bgStatusColors } from "@/styles/colors";

type LeaveRequestDetailProps = {
  leaveRequest: LeaveRequest | null;
  loading: boolean;
  userRole?: Role;
  onUpdated?: () => void;
};

export default function LeaveRequestDetail({
  leaveRequest,
  loading,
  userRole,
  onUpdated
}: LeaveRequestDetailProps) {
  const [comment, setComment] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (loading) {
    return <p>Chargement...</p>;
  }
  
  if (!leaveRequest) {
    return (
      <p className="text-sm text-red-500">
        Erreur lors du chargement de la demande.
      </p>
    );
  }

  const managerDecision = leaveRequest?.reviews.find(
    (r) => r.reviewerRole === "MANAGER"
  )?.decision;

  const managerComment = leaveRequest?.reviews.find(
    (r) => r.reviewerRole === "MANAGER"
  )?.comment;

  const hrDecision = leaveRequest?.reviews.find(
    (r) => r.reviewerRole === "HR"
  )?.decision;

  const hrComment = leaveRequest?.reviews.find(
    (r) => r.reviewerRole === "HR"
  )?.comment;


  const canManagerReview =
    leaveRequest.workflowStatus === "PENDING_MANAGER" &&
    userRole === "MANAGER";

  const canHrReview =
    leaveRequest.workflowStatus === "PENDING_HR" &&
    userRole === "HR";

  const canReview = canManagerReview || canHrReview;

  async function handleDecision(
    decision: "APPROVED" | "REJECTED"
  ) {
    try {
      if (!leaveRequest) return;
      setIsSubmitting(true);

      await createLeaveRequestReview(leaveRequest.leaveRequestId, {
        decision,
        comment,
      });

      onUpdated?.();
    } catch (err) {
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  const colorStatus =  bgStatusColors[leaveRequest?.status ?? ""] ?? "bg-gray-200";

  return (
    <>
      <p className="font-md mb-2">
        <span className="font-semibold">Statut : </span> 
        <span className={`px-2 py-1 rounded-xs text-xs font-semibold ${colorStatus}`}>{leaveRequest?.statusLabel}</span>
      </p>

          <p className="text-xs mb-4">
            Demande créée le {formatDate(leaveRequest.createdAt)}
          </p>
          <p>
            <span className="font-semibold">Type d'absence : </span>
            {leaveRequest.leaveTypeLabel}
          </p>
          <p>
          <span className="font-semibold">Période : </span>
            Du {formatDate(leaveRequest.startDate)} (
            {formatPeriod(leaveRequest.startPeriod)}) au{" "}
            {formatDate(leaveRequest.endDate)} (
            {formatPeriod(leaveRequest.endPeriod)})
          </p>

          <p>
            <span className="font-semibold">Nombre de jours : </span>
            {leaveRequest.numberOfDays}
          </p>

          {leaveRequest.employeeComment &&
            !(leaveRequest.employeeComment === "") && (
              <p>
                <span className="font-semibold">
                  Commentaire du collaborateur :
                </span>{" "}
                {leaveRequest.employeeComment}
              </p>
            )}

          {managerDecision && (
            <p className="my-1 font-semibold">
              Décision du Manager : {formatLeaveReview(managerDecision)}
            </p>
          )}
          {managerComment && (
            <p className="my-1 font-semibold">
              Commentaire du Manager : {formatLeaveReview(managerComment)}
            </p>
          )}
          {hrDecision && (
            <p className="my-1 font-semibold">
              Décision des Ressources humaines : {formatLeaveReview(hrDecision)}
            </p>
          )}
          {hrComment && (
            <p className="my-1 font-semibold">
              Commentaire des Ressources humaines :{" "}
              {formatLeaveReview(hrComment)}
            </p>
          )}


{canReview && (
        <>
          <div className="flex flex-col gap-2 mt-8">
            <label className="font-semibold text-sm">
              Ajouter un commentaire (facultatif)
            </label>

            <textarea
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Entrez votre commentaire"
              className="border border-current rounded-md p-2 text-sm resize-none h-24"
            />
          </div>

          <div className="mt-8 flex flex-col items-center lg:flex-row lg:justify-center gap-4">
            <Button
              title="Valider"
              onClick={() => handleDecision("APPROVED")}
              isLoading={isSubmitting}
              className="bg-[var(--color-light-green)] hover:bg-[var(--color-dark-green)]"
            />

            <Button
              title="Refuser"
              onClick={() => handleDecision("REJECTED")}
              isLoading={isSubmitting}
              className="bg-[var(--color-block-red)] hover:bg-[var(--color-block-red-hover)]"
            />
          </div>
        </>
      )}

    </>
  );
}
