import CategoryIcon from '../postCookie/CategoryIcon.jsx';

const SelectButton = ({ name, value, children, className, onClick, ...props }) => {
  const isSelected = value === name;

  return (
    <button
      name={name}
      type='button'
      onClick={onClick}
      className={`flex flex-col justify-center items-center text-[5px] w-[80px] h-[80px] p-5 text-sm ${isSelected ? '-bg--primary-orange text-white' : 'bg-gray-300'} ${className}`}
      {...props}
    >
      <CategoryIcon category={name} />
      {children.includes('/') ? (
        <>
          {children.split('/')[0]} /
          <br />
          {children.split('/')[1]}
        </>
      ) : (
        children
      )}
    </button>
  );
};

export default SelectButton;
