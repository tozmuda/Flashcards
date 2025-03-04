import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import Select from "../../common/Select";

const options = [
  { id: 0, name: "Option 1" },
  { id: 1, name: "Option 2" },
  { id: 2, name: "Option 3" },
];

describe("Select component", () => {
  test("renders Select with label and options", () => {
    render(
      <Select
        options={options}
        value={0}
        onChange={() => {}}
        labelText="Select an Option"
      />
    );

    expect(screen.getByText("Select an Option")).toBeInTheDocument();

    expect(screen.getByRole("combobox")).toBeInTheDocument();

    options.forEach((option) => {
      expect(screen.getByText(option.name)).toBeInTheDocument();
    });
  });

  test("displays the correct initial value", () => {
    render(<Select options={options} value={1} onChange={() => {}} />);

    expect(screen.getByDisplayValue("Option 2")).toBeInTheDocument();
  });

  test("calls onChange with the correct value when an option is selected", () => {
    const handleChange = jest.fn();
    render(<Select options={options} value={0} onChange={handleChange} />);

    fireEvent.change(screen.getByRole("combobox"), { target: { value: "2" } });

    expect(handleChange).toHaveBeenCalledWith(2);
  });
});
