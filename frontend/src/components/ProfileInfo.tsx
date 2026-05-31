import { EmployeeProfile, EmployeeDetails } from "@/types/employee/employee";
import Subtitle from "./ui/Subtitle";

type ProfileInfoProps = {
  profile: EmployeeProfile | EmployeeDetails;
};

export default function ProfileInfo({ profile }: ProfileInfoProps) {
  return (
    <>
      <Subtitle subtitle="Mes informations personnelles" />
      <p className="mb-2">
        <strong>Matricule RH :</strong> {profile.employeeNumber}
      </p>
      <p className="mb-2">
        <strong>Nom de famille :</strong> {profile.surname}
      </p>
      <p className="mb-2">
        <strong>Prénom(s) :</strong> {profile.firstName}
      </p>

      <p className="mb-2">
        <strong>E-mail :</strong> {profile.email}
      </p>

      {profile.phoneNumber && (
        <p className="mb-2">
          <strong>Numéro de téléphone :</strong> {profile.phoneNumber}
        </p>
      )}
    </>
  );
}
