import { EmployeeProfile, EmployeeDetails } from "@/types/employee/employee";
import Subtitle from "./ui/Subtitle";
import { formatDate } from "@/utils/formatDate";

type ProfileInfoProps = {
  profile: EmployeeProfile | EmployeeDetails;
  subtitle: string;
};

export default function ProfileInfo({
  profile,
  subtitle = "",
}: ProfileInfoProps) {
  return (
    <>
      <Subtitle subtitle={subtitle} />
      {profile.employeeNumber && (
        <p className="mb-2">
          <strong>Matricule RH :</strong> {profile.employeeNumber}
        </p>
      )}

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

      <p className="mb-2">
        <strong>Date d'ancienneté :</strong> {formatDate(profile.seniorityDate)}
      </p>

      <p className="mb-2">
        <strong>Département :</strong> {profile.departmentName}
      </p>

      {profile.teams && (
        <p className="mb-2">
          <strong>Equipe(s) :</strong> {profile.teams.join(", ")}
        </p>
      )}
    </>
  );
}
