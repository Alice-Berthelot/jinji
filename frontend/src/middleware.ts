import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { jwtVerify } from "jose";

const SECRET = new TextEncoder().encode(process.env.JWT_SECRET!);

export default async function middleware(request: NextRequest) {
  const pathname = request.nextUrl.pathname;

  const isPublic =
    pathname.startsWith("/login") ||
    pathname.startsWith("/api") ||
    pathname.startsWith("/_next") ||
    pathname === "/favicon.ico";

  if (isPublic) return NextResponse.next();

  const token = request.cookies.get("access_token")?.value;

  if (!token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  try {
    const { payload } = await jwtVerify(token, SECRET);
    const roles = (payload.roles ?? []) as string[];
    if (pathname.startsWith("/hr") && !roles.includes("HR")) {
      const redirectUrl = new URL("/", request.url);
      redirectUrl.searchParams.set("error", "HR_REQUIRED");
      return NextResponse.redirect(redirectUrl);
    }
    if (pathname.startsWith("/manager") && !roles.includes("MANAGER")) {
      const redirectUrl = new URL("/", request.url);
      redirectUrl.searchParams.set("error", "MANAGER_REQUIRED");
      return NextResponse.redirect(redirectUrl);
    }
    return NextResponse.next();
  } catch {
    return NextResponse.redirect(new URL("/login", request.url));
  }
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
};
