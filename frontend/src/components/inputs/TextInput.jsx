const TextInput = ({ value, setValue, className, ...props }) => {
  const handleChange = (e) => {
    setValue(e.target.value);
  };

  return (
    <input
      type='text'
      value={value}
      onChange={handleChange}
      className={className ?? null}
      {...props}
    />
  );
};

export default TextInput;
