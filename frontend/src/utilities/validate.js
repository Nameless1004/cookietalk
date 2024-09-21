const onlyBlankRegex = /^\s*$/;

export const newSeiresValidate = (input, seriesList) => {
  if (onlyBlankRegex.test(input)) {
    return {
      isValid: false,
      message: '시리즈명을 입력해주세요.',
    };
  }

  if (seriesList.some((series) => series === input)) {
    return {
      isValid: false,
      message: '이미 등록된 시리즈입니다.',
    };
  }

  return {
    isValid: true,
    message: null,
  };
};
