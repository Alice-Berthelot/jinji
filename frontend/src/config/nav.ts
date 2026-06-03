import { LuBriefcaseBusiness, LuBuilding2, LuCalendarDays, LuClipboardList, LuHouse, LuSettings2, LuUserRound, LuUsers } from "react-icons/lu";

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
    name: "Mes informations",
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
    name: "Liste des collaborateurs",
    link: "/manager/employees",
    icon: LuUsers,
  },
  // {
  //   name: "Planning de l'équipe",
  //   link: "/manager/planning-equipe",
  // },
];

export const hrLinks = [
  {
    name: "Accueil RH",
    link: "/hr",
    icon: LuBuilding2,
  },
  {
    name: "Gestion des demandes d'absences",
    link: "/hr/leaves/leave-requests",
    icon: LuClipboardList,
  },
  {
    name: "Gestion des collaborateurs",
    link: "/hr/employees",
    icon: LuUsers,
  },
  {
    name: "Paramétrage des règles RH",
    link: "/hr/policies",
    icon: LuSettings2,
  },
];
