export const API_BASE_URL = "http://localhost:8080";

export interface TextType {
  text: string;
}

export interface LanguageType {
  languageShort: string;
}

export type NotFilledFlashcardsType = string[];

export interface FilledFlashcardType {
  original: string;
  translation: string;
  baseForm: string;
  transliteration: string;
  transcription: string;
  partOfSpeech: string;
}

export interface MessageType {
  message: string;
}

type FlashcardType = {
  original: string;
  baseForm: string;
  partOfSpeech: string;
  translation: string;
  transliteration: string;
  transcription: string;
};

export type FlashcardTypeExtended = FlashcardType & { partOfSentence: string };

export type WordsWithPartsOfSentenceType = {
  simpleSentences: {
    words: {
      originalText: string;
      possiblePartsOfSentence: string[];
      selectedPartOfSentence?: string;
    }[];
    level?: number;
  }[];
}[];

export type FilledWordWithPartOfSentenceType = {
  level: number;
  selectedPartsOfSentence: (string | null)[];
}[][];

export type SentenceType = {
  words: FlashcardTypeExtended[];
  level: number;
}[][];

export type LanguagesType = { languageName: string; languageShort: string }[];
