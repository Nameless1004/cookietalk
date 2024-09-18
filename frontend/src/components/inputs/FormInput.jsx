const FormInput = ({ name, label, type, className, formValue, setFormValue, ...props }) => {
  const handleFormChange = (e) => {
    setFormValue({ ...formValue, [e.target.name]: e.target.value });
  };

  return (
    <div className='flex gap-5'>
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
