"use client";

import {
  createLeaveRequestReview,
  cancelLeaveRequest,
} from "@/services/leaveRequest.service";
import { LeaveRequest } from "@/types/leave/leaveRequest";
import { formatDate } from "@/utils/formatDate";
import { formatLeaveReview } from "@/utils/formatLeaveReview";
import { formatPeriod } from "@/utils/formatPeriod";
import { useState, useTransition } from "react";
import Button from "./ui/Button";
import { bgStatusColors } from "@/styles/colors";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { LeaveBalance } from "@/types/leave/leaveBalance";
import { getEmployeeLeaveBalances } from "@/services/leaveBalance.service";
import LeaveBalanceTable from "./tables/LeaveBalanceTable";
import { TiArrowSortedDown, TiArrowSortedUp } from "react-icons/ti";

type LeaveRequestDetailProps = {
  leaveRequest: LeaveRequest | null;
  loading?: boolean;
  userRole?: Role;
  canCancel?: boolean;
};

export default function LeaveRequestDetail({
  leaveRequest,
  loading,
  userRole,
  canCancel,
}: LeaveRequestDetailProps) {
  const [comment, setComment] = useState("");
  const [leaveBalances, setLeaveBalances] = useState<LeaveBalance[] | null>(
    null
  );
  const [isOpenBalance, setIsOpenBalance] = useState(false);
  const [loadingBalances, setLoadingBalances] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();

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
    leaveRequest.workflowStatus === "PENDING_MANAGER" && userRole === "MANAGER";

  const canHrReview =
    leaveRequest.workflowStatus === "PENDING_HR" && userRole === "HR";

  const canReview = canManagerReview || canHrReview;

  async function handleDecision(decision: "APPROVED" | "REJECTED") {
    try {
      if (!leaveRequest) return;
      setIsSubmitting(true);

      await createLeaveRequestReview(leaveRequest.leaveRequestId, {
        decision,
        comment,
      });

      router.refresh();
    } catch (err) {
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleCancel() {
    try {
      if (!leaveRequest) return;

      setIsSubmitting(true);

      await cancelLeaveRequest(leaveRequest.leaveRequestId);
      toast.success("Demande d'absence annulée avec succès");
      router.refresh();
    } catch (err) {
      toast.error("Erreur lors de l'annulation de la demande");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleToggleBalances() {
    if (isOpenBalance) {
      setIsOpenBalance(false);
      return;
    }

    if (leaveBalances) {
      setIsOpenBalance(true);
      return;
    }

    try {
      setLoadingBalances(true);

      const balances = await getEmployeeLeaveBalances(
        String(leaveRequest?.employeeId)
      );

      setLeaveBalances(balances);
      setIsOpenBalance(true);
    } finally {
      setLoadingBalances(false);
    }
  }

  const colorStatus =
    bgStatusColors[leaveRequest?.status ?? ""] ?? "bg-gray-200";

  return (
    <>
      <p className="font-md mb-2">
        <span className="font-semibold">Statut : </span>
        <span
          className={`px-2 py-1 rounded-xs text-xs font-semibold ${colorStatus}`}
        >
          {leaveRequest?.statusLabel}
        </span>
      </p>

      <p className="text-xs mb-4">
        Demande n°{leaveRequest.leaveRequestId} - Créée le{" "}
        {formatDate(leaveRequest.createdAt)}
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
          Commentaire des Ressources humaines : {formatLeaveReview(hrComment)}
        </p>
      )}


      <button
        type="button"
        onClick={handleToggleBalances}
        disabled={loadingBalances}
        className="flex items-center gap-1 text-[var(--color-block-purple)] mt-1 disabled:opacity-50"
        aria-label={isOpenBalance ? "Masquer le solde" : "Voir le solde"}
      >
        <span className="text-[var(--color-main-font)] text-sm font-medium">
          Voir le solde
        </span>
        {isOpenBalance ? (
          <TiArrowSortedUp size={25} />
        ) : (
          <TiArrowSortedDown size={25} />
        )}
      </button>

      {isOpenBalance && leaveBalances && (
        <div className="mt-6">
          <LeaveBalanceTable leaveBalance={leaveBalances} />
        </div>
      )}

      {canCancel && leaveRequest.status == "PENDING" && (
        <div className="mt-8 flex justify-center">
          <Button
            title="Annuler la demande"
            onClick={handleCancel}
            isLoading={isSubmitting}
            className="bg-[var(--color-block-red)] hover:bg-[var(--color-block-red-hover)]"
          />
        </div>
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
