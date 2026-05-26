import { render, screen, fireEvent } from "@testing-library/react";
import Button from "@/components/ui/Button";

describe("Button", () => {
  it("renders the title correctly", () => {
    render(<Button title="Send" />);
    expect(screen.getByText("Send")).toBeInTheDocument();
  });

  it("calls onClick when clicked", () => {
    const handleClick = jest.fn();

    render(<Button title="Send" onClick={handleClick} />);
    fireEvent.click(screen.getByRole("button"));

    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it("shows loading text when isLoading is true", () => {
    render(<Button title="Send" isLoading={true} />);
    expect(screen.getByText("Chargement...")).toBeInTheDocument();
  });

  it("disables button when isLoading is true", () => {
    render(<Button title="Send" isLoading={true} />);
    expect(screen.getByRole("button")).toBeDisabled();
  });

  it("does not call onClick when button is disabled", () => {
    const handleClick = jest.fn();

    render(<Button title="Send" disabled={true} onClick={handleClick} />);
    fireEvent.click(screen.getByRole("button"));

    expect(handleClick).not.toHaveBeenCalled();
  });

  it("does not call onClick when isLoading is true", () => {
    const handleClick = jest.fn();

    render(<Button title="Send" isLoading={true} onClick={handleClick} />);
    fireEvent.click(screen.getByRole("button"));

    expect(handleClick).not.toHaveBeenCalled();
  });

  it("disables button when disabled is true", () => {
    render(<Button title="Send" disabled={true} />);
    expect(screen.getByRole("button")).toBeDisabled();
  });
});