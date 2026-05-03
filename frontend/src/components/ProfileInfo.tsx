import { EmployeeProfile } from "@/types/employee/employee";
import Subtitle from "./ui/Subtitle";

type ProfileInfoProps = {
  profile: EmployeeProfile;
};

export default function ProfileInfo({ profile }: ProfileInfoProps) {
  return (
    <>
      <Subtitle subtitle="Mes informations personnelles" />
      <div className="mb-2">
        <strong>Matricule RH :</strong> {profile.employeeNumber}
      </div>
      <div className="mb-2">
        <strong>Nom de famille :</strong> {profile.surname}
      </div>
      <div className="mb-2">
        <strong>Prénom(s) :</strong> {profile.firstName}
      </div>

      <div className="mb-2">
        <strong>E-mail :</strong> {profile.email}
      </div>

      {profile.phoneNumber && (
        <div className="mb-2">
          <strong>Numéro de téléphone :</strong> {profile.phoneNumber}
        </div>
      )}
    </>
  );
}
