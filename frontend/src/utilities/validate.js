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

export const cookiePostValidate = (input) => {
  if (onlyBlankRegex.test(input.title)) {
    return {
      isValid: false,
      message: '쿠키의 제목을 입력해주세요.',
    };
  }

  if (!input.video) {
    return {
      isValid: false,
      message: '영상을 첨부해주세요.',
    };
  }

  if (!input.category) {
    return {
      isValid: false,
      message: '카테고리를 선택해주세요.',
    };
  }

  return {
    isValid: true,
    message: null,
  };
};
