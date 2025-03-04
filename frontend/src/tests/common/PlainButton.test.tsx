import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import PlainButton from "../../common/PlainButton";

test("renders PlainButton with buttonText", () => {
  render(<PlainButton buttonText="Click Me" onClick={() => {}} />);
  expect(screen.getByText("Click Me")).toBeInTheDocument();
});

test("calls onClick when button is clicked", () => {
  const handleClick = jest.fn();
  render(<PlainButton buttonText="Click Me" onClick={handleClick} />);
  fireEvent.click(screen.getByText("Click Me"));
  expect(handleClick).toHaveBeenCalledTimes(1);
});
