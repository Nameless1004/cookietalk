const Button = ({ children, className, onClick, type = 'button', styleType, ...props }) => {
  const primaryStyle = '-bg--primary-orange text-white';
  const subButton = '-text--primary-orange border -border--primary-orange';

  // TODO: 사이즈 스타일 추가하기
  let style = '';
  switch (styleType) {
    case 'primary':
      style = primaryStyle;
      break;
    case 'sub':
      style = subButton;
      break;
  }

  return (
    <button
      type={type}
      onClick={onClick}
      className={`${className} ${style} hover:opacity-75`}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
