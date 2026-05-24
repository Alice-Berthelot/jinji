const leaveReviewFrenchLabels: Record<string, string> = {
    approved: "Accepté",
    rejected: "Refusé",
  };
  
  export function formatLeaveReview(decision: string) {
    const key = decision.toLowerCase();
  
    if (key in leaveReviewFrenchLabels) {
      return leaveReviewFrenchLabels[key];
    }
  
    return decision;
  }