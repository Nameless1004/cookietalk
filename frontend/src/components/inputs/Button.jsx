const primaryStyle = '-bg--primary-orange text-white';
const subButton = '-text--primary-orange border -border--primary-orange';

const small = 'w-[100px] rounded';
const medium = 'w-[200px] rounded';
const large = 'w-[400px] rounded';
const full = 'w-full rounded';

const Button = ({ children, className, onClick, type = 'button', sizeType, styleType, ...props }) => {
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

  let size = '';
  switch (sizeType) {
    case 'small':
      size = small;
      break;
    case 'medium':
      size = medium;
      break;
    case 'large':
      size = large;
      break;
    case 'full':
      size = full;
      break;
  }

  return (
    <button
      type={type}
      onClick={onClick}
      className={`${className} ${style} ${size} hover:opacity-75`}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
