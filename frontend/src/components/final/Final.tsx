import AddManually from "./components/AddManually";
import CompleteText from "./components/CompleteText";

type FinalProps = {
  partOfSpeechOptions: string[];
};

const Final = ({ partOfSpeechOptions }: FinalProps) => {
  return (
    <div>
      <AddManually partOfSpeechOptions={partOfSpeechOptions} />
      <CompleteText />
    </div>
  );
};

export default Final;
