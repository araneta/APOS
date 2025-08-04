export interface FlatTreeRow {
  id: string | number;
  parentId: string | number | null;
  label: string;
  [key: string]: any;
}

export interface ActionButton {
  label: string;
  action: string;
  cssClass?: string;
  icon?: string;
  tooltip?: string;
  visible?: (data: any) => boolean;
  disabled?: (data: any) => boolean;
}

export interface TreeNode {
  id: string | number;
  parentId: string | number | null;
  label: string;
  children?: TreeNode[];
  isExpanded?: boolean;
  level?: number;
  isLeaf?: boolean;
  hasChildren?: boolean;
  [key: string]: any;
}

export interface FlatNode extends TreeNode {
  level: number;
  isLeaf: boolean;
  hasChildren: boolean;
}