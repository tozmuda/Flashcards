import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import Input from "../../common/Input";

test("renders Input with placeholder", () => {
  render(<Input value="" onChange={() => {}} placeholder="Enter text" />);
  expect(screen.getByPlaceholderText("Enter text")).toBeInTheDocument();
});

test("calls onChange with input value", () => {
  const handleChange = jest.fn();
  render(<Input value="" onChange={handleChange} placeholder="Enter text" />);
  fireEvent.change(screen.getByPlaceholderText("Enter text"), {
    target: { value: "New Value" },
  });
  expect(handleChange).toHaveBeenCalledWith("New Value");
});

test("displays the correct value", () => {
  render(
    <Input value="Test Value" onChange={() => {}} placeholder="Enter text" />
  );
  expect(screen.getByDisplayValue("Test Value")).toBeInTheDocument();
});
