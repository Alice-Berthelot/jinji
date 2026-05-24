import { createLeaveAction } from "@/app/actions/createLeave";

global.fetch = jest.fn();

jest.mock("next/headers", () => ({
  cookies: () => ({
    get: () => ({ value: "fake-token" }),
  }),
}));

describe("createLeaveAction", () => {
  it("returns success when API call succeeds", async () => {
    (fetch as jest.Mock).mockResolvedValue({
      ok: true,
    });

    const formData = new FormData();
    formData.set("employeeId", "1");
    formData.set("startDate", "2024-01-01");
    formData.set("endDate", "2024-01-02");
    formData.set("startPeriod", "AM");
    formData.set("endPeriod", "PM");
    formData.set("leaveTypeCode", "HOLIDAY");

    const result = await createLeaveAction(
      { error: null },
      formData
    );

    expect(result).toEqual({
      error: null,
      success: true,
    });
  });

  it("returns error when API call fails", async () => {
    (fetch as jest.Mock).mockResolvedValue({
      ok: false,
    });

    const formData = new FormData();
    formData.set("employeeId", "1");
    formData.set("startDate", "2024-01-01");
    formData.set("endDate", "2024-01-02");
    formData.set("startPeriod", "AM");
    formData.set("endPeriod", "PM");
    formData.set("leaveTypeCode", "HOLIDAY");

    const result = await createLeaveAction(
      { error: null },
      formData
    );

    expect(result).toEqual({
      error: "Erreur lors de la création de l'absence",
    });
  });
});