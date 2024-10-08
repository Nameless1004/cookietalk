const onlyBlankRegex = /^\s*$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

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

  if (!input.thumbnail) {
    return {
      isValid: false,
      message: '썸네일을 첨부해주세요.',
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

export const authValidate = ({ input, mode }) => {
  if (onlyBlankRegex.test(input.username)) {
    return {
      isValid: false,
      message: '아이디를 입력해주세요.',
    };
  }

  if (onlyBlankRegex.test(input.password)) {
    return {
      isValid: false,
      message: '비밀번호를 입력해주세요.',
    };
  }

  if (mode === 'signup') {
    if (onlyBlankRegex.test(input.email)) {
      return {
        isValid: false,
        message: '이메일을 입력해주세요.',
      };
    }

    if (!emailRegex.test(input.email)) {
      return {
        isValid: false,
        message: '이메일 형식을 확인해주세요.',
      };
    }

    if (onlyBlankRegex.test(input.nickname)) {
      return {
        isValid: false,
        message: '닉네임을 입력해주세요',
      };
    }
  }

  return {
    isValid: true,
    message: null,
  };
};

export const channelProfileValidate = (input) => {
  if (onlyBlankRegex.test(input.blogUrl)) {
    return {
      isValid: false,
      message: '블로그 주소를 입력해주세요.',
    };
  }

  if (onlyBlankRegex.test(input.businessEmail)) {
    return {
      isValid: false,
      message: '이메일 주소를 입력해주세요.',
    };
  }

  if (onlyBlankRegex.test(input.githubUrl)) {
    return {
      isValid: false,
      message: '깃허브 주소를 입력해주세요.',
    };
  }

  if (onlyBlankRegex.test(input.description)) {
    return {
      isValid: false,
      message: '채널 소개를 입력해주세요.',
    };
  }

  return {
    isValid: true,
    message: null,
  };
};
