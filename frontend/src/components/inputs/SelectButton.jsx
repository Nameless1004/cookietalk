const SelectButton = ({ name, value, children, className, onClick, ...props }) => {
  const isSelected = value === name;

  return (
    <button
      name={name}
      type='button'
      onClick={onClick}
      className={`w-[80px] h-[80px] ${isSelected ? 'bg-gray-400' : 'bg-gray-300'} ${className}`}
      {...props}
    >
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
