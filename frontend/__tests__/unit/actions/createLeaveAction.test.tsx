import { createLeaveAction } from "@/app/actions/createLeave";
import apiFetch from "@/lib/apiFetch";
jest.mock("@/lib/apiFetch", () => ({
  __esModule: true,
  default: jest.fn(),
}));

jest.mock("@/lib/auth", () => ({
  getAccessToken: jest.fn().mockResolvedValue("fake-token"),
  refreshTokens: jest.fn().mockResolvedValue("new-token"),
  deleteTokens: jest.fn(),
}));

jest.mock("@/app/actions/logout", () => ({
  logout: jest.fn(),
}));

const mockedApiFetch = apiFetch as jest.Mock;

describe("createLeaveAction", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const buildFormData = () => {
    const formData = new FormData();
    formData.set("employeeId", "1");
    formData.set("startDate", "2026-01-01");
    formData.set("endDate", "2026-01-02");
    formData.set("startPeriod", "AM");
    formData.set("endPeriod", "PM");
    formData.set("leaveTypeCode", "CP");
    return formData;
  };

  it("returns success when API call succeeds", async () => {
    mockedApiFetch.mockResolvedValue({});

    const result = await createLeaveAction(
      { error: null },
      buildFormData()
    );

    expect(mockedApiFetch).toHaveBeenCalledWith(
      "/api/leaves",
      expect.objectContaining({
        method: "POST",
        body: JSON.stringify({
          employeeId: 1,
          startDate: "2026-07-01",
          endDate: "2026-07-02",
          startPeriod: "AM",
          endPeriod: "PM",
          leaveTypeCode: "CP",
        }),
      })
    );

    expect(result).toEqual({
      error: null,
      success: true,
    });
  });

  it("returns error when API call fails with Error", async () => {
    mockedApiFetch.mockRejectedValue(new Error("API KO"));

    const result = await createLeaveAction(
      { error: null },
      buildFormData()
    );

    expect(result).toEqual({
      error: "API KO",
      success: false,
    });
  });

  it("returns fallback error when thrown value is not an Error", async () => {
    mockedApiFetch.mockRejectedValue({});

    const result = await createLeaveAction(
      { error: null },
      buildFormData()
    );

    expect(result).toEqual({
      error: "Erreur lors de la création de l'absence",
      success: false,
    });
  });
});