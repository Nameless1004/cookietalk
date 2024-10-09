import CategoryIcon from '../postCookie/CategoryIcon.jsx';

const SelectButton = ({ name, value, children, className, onClick, ...props }) => {
  const isSelected = value?.name === name;

  return (
    <button
      name={name}
      type='button'
      onClick={onClick}
      className={`flex flex-col justify-center items-center w-[80px] h-[80px] text-center text-[12px] ${isSelected ? '-bg--primary-orange text-white' : 'bg-gray-300'} ${className}`}
      {...props}
    >
      <CategoryIcon
        className='w-full'
        size='40px'
        strokeWidth='1px'
        category={name}
      />
      {children}
    </button>
  );
};

export default SelectButton;
