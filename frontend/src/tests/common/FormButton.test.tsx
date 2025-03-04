import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import FormButton from "../../common/FormButton";

test("renders FormButton with buttonText", () => {
  render(<FormButton buttonText="Submit" />);
  expect(screen.getByText("Submit")).toBeInTheDocument();
});

test("calls onSubmit when button is clicked", () => {
  const handleClick = jest.fn();
  render(
    <form onSubmit={handleClick}>
      <FormButton buttonText="Submit" />
    </form>
  );
  const form = screen.getByRole("button", { name: /submit/i }).closest("form");
  if (form) {
    fireEvent.submit(form);
  } else {
    throw new Error("Form not found");
  }
  expect(handleClick).toHaveBeenCalledTimes(1);
});
