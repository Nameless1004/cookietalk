import { CopySlash, CodeXml, Infinity, Bird, Smartphone, Binary, Wifi } from 'lucide-react';

const CategoryIcon = ({ category, className, ...props }) => {
  switch (category) {
    case 'backend':
      return (
        <CopySlash
          className={`${className || ''}`}
          {...props}
        />
      );
    case 'frontend':
      return (
        <CodeXml
          className={`${className || ''}`}
          {...props}
        />
      );
    case 'devops':
      return (
        <Infinity
          className={`${className || ''}`}
          {...props}
        />
      );
    case 'iosAndroid':
      return (
        <Smartphone
          className={`${className || ''}`}
          {...props}
        />
      );
    case 'computerScience':
      return (
        <Binary
          className={`${className || ''}`}
          {...props}
        />
      );
    case 'network':
      return (
        <Wifi
          className={`${className || ''}`}
          {...props}
        />
      );
    default:
      return (
        <Bird
          className={`${className || ''}`}
          {...props}
        />
      );
  }
};

export default CategoryIcon;
