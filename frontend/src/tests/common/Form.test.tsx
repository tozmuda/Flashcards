import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import Form from "../../common/Form";

test("renders Form component with children", () => {
  render(
    <Form onSubmit={() => {}}>
      <div>Child Component</div>
    </Form>
  );
  expect(screen.getByText("Child Component")).toBeInTheDocument();
});

test("calls onSubmit when form is submitted", () => {
  const handleSubmit = jest.fn();
  render(
    <Form onSubmit={handleSubmit}>
      <div>Child Component</div>
    </Form>
  );
  fireEvent.submit(screen.getByRole("form"));
  expect(handleSubmit).toHaveBeenCalledTimes(1);
});
