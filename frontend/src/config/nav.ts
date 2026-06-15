import {
  LuBriefcaseBusiness,
  LuBuilding2,
  LuCalendarDays,
  LuCalendarRange,
  LuClipboardList,
  LuHouse,
  LuSettings2,
  LuUserRound,
  LuUsers,
} from "react-icons/lu";

export const employeeLinks = [
  {
    name: "Accueil",
    link: "/",
    icon: LuHouse,
  },
  {
    name: "Mes absences",
    link: "/leaves",
    icon: LuCalendarDays,
  },
  {
    name: "Mon profil",
    link: "/profile",
    icon: LuUserRound,
  },
];

export const managerLinks = [
  {
    name: "Accueil Manager",
    link: "/manager",
    icon: LuBriefcaseBusiness,
  },
  {
    name: "Demandes d'absence",
    link: "/manager/leaves/leave-requests",
    icon: LuClipboardList,
  },
  {
    name: "Collaborateurs",
    link: "/manager/employees",
    icon: LuUsers,
  },
  {
    name: "Planning de l'équipe",
    link: "/manager/calendar",
    icon: LuCalendarRange,
  },
];

export const hrLinks = [
  {
    name: "Accueil RH",
    link: "/hr",
    icon: LuBuilding2,
  },
  {
    name: "Absences",
    link: "/hr/leaves",
    icon: LuClipboardList,
  },
  {
    name: "Collaborateurs",
    link: "/hr/employees",
    icon: LuUsers,
  },
  {
    name: "Paramétrage des règles RH",
    link: "/hr/policies",
    icon: LuSettings2,
  },
];
