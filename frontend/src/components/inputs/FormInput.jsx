const FormInput = ({ name, label, type, className, formValue, setFormValue, ...props }) => {
  const handleFormChange = (e) => {
    setFormValue({ ...formValue, [e.target.name]: e.target.value });
  };

  return (
    <div className='grid grid-cols-[1fr_2fr]'>
      {label ? <span className='font-bold'>{label}</span> : null}
      <input
        name={name}
        type={type ?? 'text'}
        value={formValue.name}
        onChange={handleFormChange}
        className={`border rounded ${className}`}
        {...props}
      />
    </div>
  );
};

export default FormInput;
