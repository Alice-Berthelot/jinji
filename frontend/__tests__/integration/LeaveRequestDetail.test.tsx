import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

jest.mock("@/lib/apiFetch", () => ({
  __esModule: true,
  default: jest.fn(),
}));

import LeaveRequestDetail from "@/components/LeaveRequestDetail";
import { createLeaveRequestReview } from "@/services/leaveRequest.service";
import { LeaveRequest } from "@/types/leave/leaveRequest";

jest.mock("@/services/leaveRequest.service", () => ({
  createLeaveRequestReview: jest.fn(),
}));

jest.mock("next/navigation", () => ({
  useRouter: () => ({
    refresh: jest.fn(),
  }),
}));

jest.mock("@/services/leaveBalance.service", () => ({
  getEmployeeLeaveBalances: jest.fn(),
}));

const mockLeaveRequest: LeaveRequest = {
  leaveRequestId: 1,
  status: "PENDING",
  statusLabel: "En attente RH",
  workflowStatus: "PENDING_HR",
  leaveTypeLabel: "Congés payés",
  startDate: "2026-07-01",
  endDate: "2026-07-02",
  startPeriod: "AM",
  endPeriod: "PM",
  numberOfDays: 5,
  createdAt: "2026-01-01",
  employeeComment: "",
  reviews: [],
  employeeFirstName: "John",
  employeeSurname: "Doe",
  employeeId: 2,
};

describe("LeaveRequestDetail", () => {
  it("submits an approval review when the HR user validates the request", async () => {
    const user = userEvent.setup();

    render(
      <LeaveRequestDetail
        leaveRequest={mockLeaveRequest as LeaveRequest}
        userRole="HR"
        loading={false}
      />
    );

    const textarea = screen.getByPlaceholderText("Entrez votre commentaire");

    await user.type(textarea, "OK");

    const button = screen.getByText("Valider");

    await user.click(button);

    expect(createLeaveRequestReview).toHaveBeenCalledWith(1, {
      decision: "APPROVED",
      comment: "OK",
    });
  });
});