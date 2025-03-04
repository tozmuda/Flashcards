import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import TextArea from "../../common/TextArea";

test("renders TextArea with placeholder", () => {
  render(<TextArea text="" onChange={() => {}} placeholder="Enter text" />);
  expect(screen.getByPlaceholderText("Enter text")).toBeInTheDocument();
});

test("calls onChange with textarea value", () => {
  const handleChange = jest.fn();
  render(<TextArea text="" onChange={handleChange} placeholder="Enter text" />);
  fireEvent.change(screen.getByPlaceholderText("Enter text"), {
    target: { value: "New Value" },
  });
  expect(handleChange).toHaveBeenCalledWith("New Value");
});

test("displays the correct value", () => {
  render(
    <TextArea text="Test Value" onChange={() => {}} placeholder="Enter text" />
  );
  expect(screen.getByDisplayValue("Test Value")).toBeInTheDocument();
});
